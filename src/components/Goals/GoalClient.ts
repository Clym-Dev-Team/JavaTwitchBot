import {useEffect, useState} from "react";

export async function getGoal(): Promise<Goal> {
  const r = await fetch("http://localhost:80/donation_goals");
  if (!r.ok) {
    console.log(r.status)
    console.log(r.statusText)
  }
  return r.json();
}

export async function saveGoal(goal: Goal): Promise<void> {
  const r = await fetch("http://localhost:80/donation_goals", {method: "POST", body: JSON.stringify(goal)});
  if (!r.ok) {
    console.log(r.status)
    console.log(r.statusText)
  }
}

const useGoal = () => {
  const [goal, setGoal] = useState<Goal>(goal);

  useEffect(() => {
    getGoal()
      .then(r => setGoal(r))
      .catch(reason => console.log(reason))
  }, [])
  return
}