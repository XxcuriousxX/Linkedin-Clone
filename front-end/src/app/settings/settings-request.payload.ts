export interface SettingsRequestPayload {
  email: string;
  password: string;
  username: string;
}

export interface ChangeProfileImageRequestPayload{
  username: string;
  file : File;
}
