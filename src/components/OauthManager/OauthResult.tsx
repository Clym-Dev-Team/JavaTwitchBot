import useQuery from "../../common/useQuery.ts";
import {Link} from "react-router-dom";
import {buttonVariants} from "@shadcn/components/ui/button.tsx";
import "./OauthResult.css"

export default function OauthResult() {
  const query = useQuery();
  const success: boolean | null = query.get("success")
  const error: string | null= query.get("error");

  return <div className="oauthResult">
    <div className="resultContent">
      <span className="header">{success ? "Authentifizierung erfolgreich!"  : "Authentifizierung gescheitert!"}</span>
      {error ? <span className="errorBox">Error: {error}</span> : ""}
      <Link className={buttonVariants({variant: "default"}) + " backBtn"} to="/oauth">Return</Link>
    </div>
  </div>
}