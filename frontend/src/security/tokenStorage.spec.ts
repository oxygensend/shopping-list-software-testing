import {
    setAccessToken,
    setRefreshToken,
    getAccessToken,
    getRefreshToken,
    removeTokens,
    getPayload
} from './tokenStorage'; // Assuming the file containing the functions is named 'auth.js'

describe('Token storage', () => {
    beforeEach(() => {
        // Clear localStorage before each test
        window.localStorage.clear();
    });

    test('setAccessToken should set access token in localStorage', () => {
        setAccessToken('access-token');
        expect(window.localStorage.getItem('accessToken')).toBe('access-token');
    });

    test('setRefreshToken should set refresh token in localStorage', () => {
        setRefreshToken('refresh-token');
        expect(window.localStorage.getItem('refreshToken')).toBe('refresh-token');
    });

    test('getAccessToken should return access token from localStorage', () => {
        window.localStorage.setItem('accessToken', 'test-access-token');
        expect(getAccessToken()).toBe('test-access-token');
    });

    test('getRefreshToken should return refresh token from localStorage', () => {
        window.localStorage.setItem('refreshToken', 'test-refresh-token');
        expect(getRefreshToken()).toBe('test-refresh-token');
    });

    test('removeTokens should remove tokens from localStorage', () => {
        window.localStorage.setItem('accessToken', 'test-access-token');
        window.localStorage.setItem('refreshToken', 'test-refresh-token');
        removeTokens();
        expect(window.localStorage.getItem('accessToken')).toBe(null);
        expect(window.localStorage.getItem('refreshToken')).toBe(null);
    });

    test('getPayload should decode and return payload from access token', () => {
        const mockToken =
            'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c';
        window.localStorage.setItem('accessToken', mockToken);
        const decodedPayload = getPayload();
        expect(decodedPayload).toEqual({
            sub: '1234567890',
            name: 'John Doe',
            iat: 1516239022
        });
    });

    test('getPayload should return null if access token is not present', () => {
        const decodedPayload = getPayload();
        expect(decodedPayload).toBe(null);
    });
});