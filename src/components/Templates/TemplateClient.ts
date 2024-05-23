import {Template} from "./Template.ts";

export async function getAllTemplates(): Promise<Template[]> {
  const r  = await fetch("http://localhost:80/templates/all");
  if(!r.ok) {
    console.log(r.status)
    console.log(r.statusText)
  }
  return r.json();
}

export async function getTemplateById(module: string, type: string, object: string): Promise<Template> {

}

export async function saveTemplate(template: Template): Promise<void> {

}