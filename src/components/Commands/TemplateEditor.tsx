import {StringTemplate} from "./Command.ts";
import {Textarea} from "@shadcn/components/ui/textarea.tsx";

export interface TemplateEditorProps {
  template: StringTemplate
}

export default function TemplateEditor({template}: TemplateEditorProps) {
  return <div className="templateEditor">
    <div className="templateVars">
      {template.vars.map((tVar) => <span>{tVar.name}: {tVar.type}</span>)}
    </div>
    <div className="templateBody">
      <Textarea placeholder={"Enter Template here"} value={template.template}/>
    </div>
  </div>
}