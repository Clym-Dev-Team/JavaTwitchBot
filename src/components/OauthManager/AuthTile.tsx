import "./OauthResult.tsx"
import {Oauth} from "./OauthSetup.tsx";
import {Link} from "react-router-dom";
import "./AuthTile.css"

export interface AuthTileProps {
  oauth: Oauth
}

export default function AuthTile({oauth}: AuthTileProps) {
  return <Link to={oauth.authUrl}>
    <div className={"authTile"}>
      <span className="serviceName">{oauth.serviceName}</span>
      <span className="accountName">{oauth.accountName}</span>
    </div>
  </Link>
}