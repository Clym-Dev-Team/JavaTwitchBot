import {Template} from "./Template.ts";
import {fetchWithAuth} from "../Login/LoginPage.tsx";
import {BOT_BACKEND_ADDR} from "../../main.tsx";

export async function getAllTemplates(): Promise<Template[]> {
  const r  = await fetchWithAuth(`${BOT_BACKEND_ADDR}/templates/all`);
  if(!r.ok) {
    console.log(r.status)
    console.log(r.statusText)
  }
  return r.json();
}

export async function getTemplateById(module: string, type: string, object: string): Promise<Template> {
  const params = new URLSearchParams({
      module: module,
      type: type,
      object: object,
    }
  )
  const r  = await fetchWithAuth( `${BOT_BACKEND_ADDR}/templates?` + params);
  if(!r.ok) {
    console.log(r.status)
    console.log(r.statusText)
  }
  return r.json();
}

export async function saveTemplate(template: Template): Promise<void> {
  const params = new URLSearchParams({
    module: template.module,
    type: template.type,
    object: template.object,
    }
  )
  const r  = await fetchWithAuth(`${BOT_BACKEND_ADDR}/templates?` + params, {method: "POST", body: template.template});
  if(!r.ok) {
    console.log(r.status)
    console.log(r.statusText)
  }
}