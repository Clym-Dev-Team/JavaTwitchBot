'use client';

import {useEffect, useState} from "react";
import {getGoal, saveGoal} from "./GoalClient.ts";
import Loader from "../../common/LoadingSpinner/Loader.tsx";
import {useForm} from "react-hook-form";
import "./GoalEditor.css"
import TitleBar from "../TitleBar/TitleBar.tsx";

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
    saveGoal(goal).then(() => console.log("goal saved")); //TODO show toast os smth
  }

  return (
    <div className="goalEditor">
      <TitleBar title="Edit Active Donation Goal:"/>
      <div className="goalEditor-content">
      {loading ? <Loader/> :
        <form onSubmit={handleSubmit(submit)}>
          <label className="goalForm">Name:
            <input type="text" {...register("name", {required: true})}/>
          </label>
          <label className="goalForm">current:
            <input step="0.01" type="number" {...register("current", {required: true, valueAsNumber: true, min: 0})}/>
          </label>
          <label className="goalForm">target:
            <input step="0.01" type="number" {...register("target", {required: true, valueAsNumber: true, min: 0})}/>
          </label>
          <label className="goalForm">alertText:
            <input type="text" {...register("alertText", {required: true})}/>
          </label>
          <button className="goalForm-save" id="submit" name="submit" type="submit">Save</button>
        </form>
      }
      </div>
    </div>
  )
}