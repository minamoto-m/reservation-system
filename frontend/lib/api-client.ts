/**
 * API Base URLを環境に応じて取得
 */
export function getApiUrl(): string {
    // サーバーサイド（SSR）での実行
    if (typeof window === 'undefined') {
      // Docker内部のURL
      return process.env.API_URL || 'http://nginx/api';
    }
    
    // クライアントサイド（CSR）での実行
    if (process.env.NODE_ENV === 'development') {
      // 開発時：バックエンドに直接アクセス
      return process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';
    }
    
    // 本番時：nginx経由
    return '/api';
  }
  
  /**
   * 汎用的なfetch関数
   */
  export async function fetchApi<T>(
    endpoint: string,
    options?: RequestInit
  ): Promise<T> {
    const baseUrl = getApiUrl();
    const url = `${baseUrl}${endpoint}`;
    
    // デバッグ用ログ（開発時のみ）
    if (process.env.NODE_ENV === 'development') {
      console.log(`[API Request] ${options?.method || 'GET'} ${url}`);
    }
    
    const res = await fetch(url, {
      ...options,
      credentials: 'include',  // HTTP-only Cookie を送受信
      headers: {
        'Content-Type': 'application/json',
        ...options?.headers,
      },
    });
  
    if (!res.ok) {
      const errorText = await res.text();
      if (res.status === 401 && typeof window !== 'undefined') {
        // ログイン/ログアウト自体の 401（認証失敗）ではリダイレクトしない（エラーを画面に表示するため）
        const isAuthEndpoint = endpoint.includes('/auth/login') || endpoint.includes('/auth/logout');
        if (!isAuthEndpoint) {
          window.location.href = '/login';
        }
        throw new Error(errorText || '認証が必要です');
      }
      throw new Error(errorText || `API Error ${res.status}`);
    }
  
    // 204 No Content の場合
    if (res.status === 204) {
      return null as T;
    }

    const text = await res.text();
    if (!text) {
      return null as T;
    }
    return JSON.parse(text) as T;
  }