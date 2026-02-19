import { fetchApi, getApiUrl } from '@/lib/api-client';

export type AuthUser = { username: string };

/**
 * 認証 API
 */
export const authApi = {
  /**
   * 認証状態を確認。認証済みならユーザー情報を返し、未認証なら 401 で fetchApi がログインへリダイレクト
   */
  check: async (): Promise<AuthUser> => {
    return fetchApi<AuthUser>('/v1/auth/me');
  },

  /**
   * ログイン（JWT は HTTP-only Cookie で設定される）
   */
  login: async (username: string, password: string): Promise<void> => {
    await fetchApi<void>('/v1/auth/login', {
      method: 'POST',
      body: JSON.stringify({ username, password }),
    });
  },

  /**
   * ログアウト（Cookie を削除）
   */
  logout: async (): Promise<void> => {
    await fetchApi<void>('/v1/auth/logout', {
      method: 'POST',
    });
  },

  /**
   * 会員登録。成功時は 204。メール重複時は 409 で message を throw
   */
  register: async (email: string, password: string): Promise<void> => {
    const res = await fetch(`${getApiUrl()}/v1/auth/register`, {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password }),
    });
    if (res.ok) return;
    const text = await res.text();
    if (res.status === 409) {
      try {
        const body = JSON.parse(text) as { message?: string };
        throw new Error(body.message ?? 'このメールアドレスは既に登録されています');
      } catch (e) {
        if (e instanceof Error) throw e;
        throw new Error('このメールアドレスは既に登録されています');
      }
    }
    throw new Error(text || `登録に失敗しました (${res.status})`);
  },
};
