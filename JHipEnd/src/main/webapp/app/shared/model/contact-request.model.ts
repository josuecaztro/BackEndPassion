export interface IContactRequest {
  id?: number;
  messageBody?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  phoneNumber?: string | null;
}

export const defaultValue: Readonly<IContactRequest> = {};
