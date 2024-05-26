import {Template} from "../Template.ts";
import {useForm} from "react-hook-form";
import "./TemplateForm.css"
import {Link} from "react-router-dom";

export interface TemplateFormProps {
  template: Template,
  onSave: (Template: Template) => void,
  exitRedirect: string,
}

export default function TemplateForm({template, onSave, exitRedirect}: TemplateFormProps) {
  const {handleSubmit, register, reset} = useForm<Template>({defaultValues: template})

  function submit(template: Template) {
    onSave(template);
  }

  return <form onSubmit={handleSubmit(submit)}>
    <div className="templateForm-fields">
      <label className="fieldLabel">
        <div className="fieldTitle">Identifier:</div>
        <div className="templateForm-idFields">
          <input {...register("module")} readOnly={true}/>
          <input {...register("type")} readOnly={true}/>
          <input {...register("object")} readOnly={true}/>
        </div>
      </label>
      <label className="fieldLabel">
        <div className="fieldTitle">Template String:</div>
        <input {...register("template")}/>
      </label>
    </div>
    <div className="formBtns">
      <button onMouseDown={handleSubmit(submit)} type="submit">Save</button>
      <button onMouseDown={() => reset(template)} type="reset">Reset</button>
      <Link to={exitRedirect}>Cancel</Link>
    </div>
  </form>
}