import {AuthContext, fetchWithAuth} from "../Login/AuthProvider.tsx";
import {BOT_BACKEND_ADDR} from "../../main.tsx";

export async function getGoal(context: AuthContext): Promise<Goal> {
  const r = await fetchWithAuth(context,`${BOT_BACKEND_ADDR}/donation_goals`);
  if (!r.ok) {
    console.log(r.status)
    console.log(r.statusText)
  }
  return r.json();
}

export async function saveGoal(context: AuthContext, goal: Goal): Promise<Response> {
  return await fetchWithAuth(context, `${BOT_BACKEND_ADDR}/donation_goals`, {
    method: "POST",
    body: JSON.stringify(goal)
  });
}