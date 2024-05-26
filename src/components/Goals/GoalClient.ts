import {AuthContext, fetchWithAuth} from "../Login/AuthProvider.tsx";

export async function getGoal(context: AuthContext): Promise<Goal> {
  const r = await fetchWithAuth(context,"http://localhost:80/donation_goals");
  if (!r.ok) {
    console.log(r.status)
    console.log(r.statusText)
  }
  return r.json();
}

export async function saveGoal(context: AuthContext, goal: Goal): Promise<void> {
  const r = await fetchWithAuth(context,"http://localhost:80/donation_goals", {method: "POST", body: JSON.stringify(goal)});
  if (!r.ok) {
    console.log(r.status)
    console.log(r.statusText)
  }
}