import {Template} from "./Template.ts";
import {AuthContext, fetchWithAuth} from "../Login/AuthProvider.tsx";

export async function getAllTemplates(context: AuthContext): Promise<Template[]> {
  const r  = await fetchWithAuth(context,"http://localhost:80/templates/all");
  if(!r.ok) {
    console.log(r.status)
    console.log(r.statusText)
  }
  return r.json();
}

export async function getTemplateById(context: AuthContext, module: string, type: string, object: string): Promise<Template> {
  const params = new URLSearchParams({
      module: module,
      type: type,
      object: object,
    }
  )
  const r  = await fetchWithAuth(context, "http://localhost:80/templates?" + params);
  if(!r.ok) {
    console.log(r.status)
    console.log(r.statusText)
  }
  return r.json();
}

export async function saveTemplate(context: AuthContext, template: Template): Promise<void> {
  const params = new URLSearchParams({
    module: template.module,
    type: template.type,
    object: template.object,
    }
  )
  const r  = await fetchWithAuth(context,"http://localhost:80/templates?" + params, {method: "POST", body: template.template});
  if(!r.ok) {
    console.log(r.status)
    console.log(r.statusText)
  }
}