'use client';

import {useEffect, useState} from "react";
import {getGoal, saveGoal} from "./GoalClient.ts";
import Loader from "../../common/LoadingSpinner/Loader.tsx";
import {useForm} from "react-hook-form";
import TitleBar from "../TitleBar/TitleBar.tsx";
// import {useBlocker} from "react-router-dom";
import {useToast} from "@shadcn/components/ui/use-toast.ts";
import "./GoalEditor.css"

export function GoalEditorPane() {
  const [loading, setLoading] = useState(true);
  const {handleSubmit, register, reset, formState} = useForm<Goal>();
  // const blocker = useBlocker(formState.isDirty);
  const toast = useToast();

  useEffect(() => {
    getGoal()
      .then(r => reset(r))
      .catch(reason => toast.toast(
        {className: "toast toast-failure", title: "ERROR Loading Goal", description: reason.toString()}))
      .finally(() => setLoading(false));
  }, [])

  function submit(goal: Goal) {
    reset(goal);
    saveGoal(goal)
      .then(() => toast.toast(
        {className: "toast toast-success", title: "Goal saved Successfully!"}))
      .catch(reason => toast.toast(
        {className: "toast toast-failure", title: "ERROR saving Goal", description: reason.toString()})
      );
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
            <button className="goalForm-save" type="submit" disabled={!formState.isDirty}>Save</button>
          </form>
        }
      </div>
    </div>
  )
}