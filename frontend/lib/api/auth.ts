import { fetchApi } from '@/lib/api-client';

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
};
