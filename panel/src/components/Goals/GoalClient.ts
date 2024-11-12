import {fetchWithAuth} from "../Login/LoginPage.tsx";
import {BOT_BACKEND_ADDR} from "../../main.tsx";

export async function getGoal(): Promise<Goal> {
  const r = await fetchWithAuth(`${BOT_BACKEND_ADDR}/donation_goals`);
  if (!r.ok) {
    console.log(r.status)
    console.log(r.statusText)
  }
  return r.json();
}

export async function saveGoal(goal: Goal): Promise<Response> {
  return await fetchWithAuth(`${BOT_BACKEND_ADDR}/donation_goals`, {
    method: "POST",
    body: JSON.stringify(goal)
  });
}