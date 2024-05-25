import {Template} from "../Template.ts";
import "./TemplateListItem.css"

export interface TemplateListItemProps {
  template: Template,
}

export default function TemplateListItem({template}: TemplateListItemProps) {
  const href = `/templates/edit?module=${template.module}&type=${template.type}&object=${template.object}`;
  return (
    <a className="template_list-item" href={href}>
      <div className="identifier">
        <span className="module">{template.module}</span>
        <span className="dot">.</span>
        <span className="name">{template.type}</span>
        <span className="dot">.</span>
        <span className="object-name">{template.object}</span>
      </div>
      <div className="template_string">{template.template}</div>
    </a>
  )

}