import {InputHealth} from "./InputHealth.ts";
import {AuthContext, fetchWithAuth} from "../Login/AuthProvider.tsx";
import {BOT_BACKEND_ADDR} from "../../main.tsx";

export async function getAllHealthStatuses(context: AuthContext): Promise<InputHealth[]> {
  const r = await fetchWithAuth(context,`${BOT_BACKEND_ADDR}/health/json`);
  if (!r.ok) {
    console.log(`failed to get all health statuses: ${r.status} ${r.statusText}`)
  }
  return r.json();
}