import useQuery from "../../../common/useQuery.ts";
import {getTemplateById, saveTemplate} from "../TemplateClient.ts";
import {useEffect, useState} from "react";
import {Template} from "../Template.ts";
import Loader from "../../LoadingSpinner/Loader.tsx";
import TemplateForm from "./TemplateForm.tsx";
import "./TemplateEditorTile.css"

export default function TemplateEditorPane() {
  const q = useQuery();
  const [template, setTemplate] = useState<Template | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const module = q.get("module");
  const type = q.get("type");
  const object = q.get("object");

  useEffect(() => {
    if (module == null || object == null || type == null) {
      setError(`Error: Request Parameters: "module", "type", "object" must all be given!`);
      setLoading(false);
    } else {
      getTemplateById(module, type, object)
        .then(template => setTemplate(template))
        .catch(err => setError(err))
        .finally(() => setLoading(false));
    }
  }, [q]);

  function handleSave(template: Template) {
    saveTemplate(template)
      .then(value => console.log(`saved template: ${value}`)) //TODO make toast
      .catch(reason => console.log(reason)) //TODO make toast
  }

  const exitRedirect = "/templates";
  return <div className="templateEditor">
    <div className="titleBar">
      <div className="templateEditor-returnBtn">
        <a href={exitRedirect}>Return</a>
      </div>
      <div className="templateEditor-title">Edit: {module}.{type}.{object}</div>
    </div>
    <div className="templateEditor-content">
      {loading && <Loader/>}
      {error && <div className="templateEditor-errContainer">{error}</div>}
      {template && <TemplateForm template={template} onSave={handleSave} exitRedirect={exitRedirect}/>}
    </div>
  </div>
}