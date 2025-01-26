export type UserRole = "admin" | "manager" | "driver";

export interface User {
    role: UserRole;
}