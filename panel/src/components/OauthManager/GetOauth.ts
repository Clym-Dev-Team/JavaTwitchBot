import {fetchWithAuth} from "../Login/LoginPage.tsx";
import {BOT_BACKEND_ADDR} from "../../main.tsx";
import {Oauth} from "./OauthSetup.tsx";

export async function getOauth(): Promise<Oauth[]> {
  const r = await fetchWithAuth(`${BOT_BACKEND_ADDR}/setup/auth/list`);
  if (!r.ok) {
    console.log(`could not load open auths: ${r.status} ${r.statusText}`);
  }
  return r.json();
}
