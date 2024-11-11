import {Textarea} from "@shadcn/components/ui/textarea.tsx";
import {StringTemplate} from "./StringTemplate.ts";
import {Tooltip, TooltipContent, TooltipTrigger} from "@shadcn/components/ui/tooltip.tsx";
import "./TemplateEditor.css"
import {Popover, PopoverContent, PopoverTrigger} from "@shadcn/components/ui/popover.tsx";

export interface TemplateEditorProps {
  template: StringTemplate
}

export default function TemplateEditor({template}: TemplateEditorProps) {
  if (template.vars === undefined) {
    template.vars = [];
  }

  const vars = Object.entries(JSON.parse(template.varJsonSchema));
  const d: {key: string, v: string[]}[] = [];
  for (const kv of vars) {
    const text = JSON.stringify(kv[1], null, 2).replaceAll("\"", "").split("\n")
    d.push({key: kv[0], v: text});
  }
  return <div className="templateEditor">
    <div className="templateVars">
      {vars.map((tVar) => <Popover>
        <PopoverTrigger>{tVar[0]}</PopoverTrigger>
        <PopoverContent className="dark popoverContent">{JSON.stringify(tVar[1], null, 4).replaceAll("\"", "")}</PopoverContent>
      </Popover>)}
    </div>
    <div className="templateBody">
      <Textarea placeholder={"Enter Template here"} value={template.template}/>
    </div>
  </div>
}