import {AuthContext, fetchWithAuth} from "../Login/AuthProvider.tsx";
import {BOT_BACKEND_ADDR} from "../../main.tsx";
import {Oauth} from "./OauthSetup.tsx";

export async function getOauth(context: AuthContext): Promise<Oauth[]> {
  const r = await fetchWithAuth(context,`${BOT_BACKEND_ADDR}/setup/auth/list`);
  if (!r.ok) {
    console.log(`could not load open auths: ${r.status} ${r.statusText}`);
  }
  return r.json();
}
