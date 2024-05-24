'use client';

import {useEffect, useState} from "react";
import {getGoal, saveGoal} from "../Goals/GoalClient.ts";
import Loader from "../LoadingSpinner/Loader.tsx";
import {useForm} from "react-hook-form";

export function GoalEditorPane() {
  const [loading, setLoading] = useState(true);
  const {handleSubmit, register, reset} = useForm<Goal>();

  useEffect(() => {
    getGoal()
      .then(r => {
        reset(r);
      })
      .catch(reason => console.log(reason))
      .finally(() => setLoading(false));
  }, [])

  function submit(goal: Goal) {
    console.log("evebt")
    console.log(goal)
    saveGoal(goal).then(() => console.log("goal saved"));
  }

  return (
    <div className="goal-editor">
      <h1>Current Donation Goal:</h1>
      {loading ? <Loader/> :
        <form onSubmit={handleSubmit(submit)}>
          <label className="goal-form">Name:
            <input type="text" {...register("name", {required: true})}/>
          </label>
          <label className="goal-form">current:
            <input step="0.01" type="number" {...register("current", {required: true, valueAsNumber: true, min: 0})}/>
          </label>
          <label className="goal-form">target:
            <input step="0.01" type="number" {...register("target", {required: true, valueAsNumber: true, min: 0})}/>
          </label>
          <label className="goal-form">alertText:
            <input type="text" {...register("alertText", {required: true})}/>
          </label>
          <button className="goal-form-save" id="submit" name="submit" type="submit">Save</button>
        </form>
      }
    </div>
  )
}