export type AccessTokenPayload = {
    username: string;
    email: string;
    name: string;
    iat: number;
    exp: number;
    type: 'auth';
};