export type FormValues = {
    email: string;
    firstName: string;
    lastName: string;
    password: string;
}

export type RegisterResponse = {
    accessToken: string;
    refreshToken: string;
}