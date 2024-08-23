export interface IPrayerRequestForm {
  id?: number;
  description?: string | null;
  timeFrame?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  phoneNumber?: string | null;
}

export const defaultValue: Readonly<IPrayerRequestForm> = {};
