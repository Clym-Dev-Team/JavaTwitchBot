import {InputHealth} from "./InputHealth.ts";
import {fetchWithAuth} from "../Login/LoginPage.tsx";
import {BOT_BACKEND_ADDR} from "../../main.tsx";

export async function getAllHealthStatuses(): Promise<InputHealth[]> {
  const r = await fetchWithAuth(`${BOT_BACKEND_ADDR}/health/json`);
  if (!r.ok) {
    console.log(`failed to get all health statuses: ${r.status} ${r.statusText}`)
  }
  return r.json();
}