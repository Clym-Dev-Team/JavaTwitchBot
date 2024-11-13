import {Textarea} from "../../../../@shadcn/components/ui/textarea.tsx";
import {StringTemplate} from "./StringTemplate.ts";
import "./TemplateEditor.css"
import {Popover, PopoverContent, PopoverTrigger} from "../../../../@shadcn/components/ui/popover.tsx";

export interface TemplateEditorProps {
  template: StringTemplate
}

function getVarsFromJson(template: StringTemplate) {
  try {
    return Object.entries(JSON.parse(template.varJsonSchema));
  } catch (e) {
    return [];
  }
}

export default function TemplateEditor({template}: TemplateEditorProps) {
  if (template.vars === undefined) {
    template.vars = [];
  }

  const vars = getVarsFromJson(template);
  return <div className="templateEditor">
    <div className="templateVars">
      {vars.map((tVar) => <Popover>
        <PopoverTrigger>{tVar[0]}</PopoverTrigger>
        <PopoverContent
          className="dark popoverContent">{JSON.stringify(tVar[1], null, 4).replaceAll("\"", "")}</PopoverContent>
      </Popover>)}
    </div>
    <div className="templateBody">
      <Textarea placeholder={"Enter Template here"} value={template.template}/>
    </div>
  </div>
}