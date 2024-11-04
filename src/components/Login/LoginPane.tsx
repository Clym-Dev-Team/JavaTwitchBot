import "./LoginPane.css"
import {Link} from "react-router-dom";


export default function LoginPane() {
  const clientId = "zmxjjn3xmncg8ewew6tjk08tub26bb";
  const baseDomain = "http://localhost:5173";
  const redirectUri = baseDomain + "/twitchToken";
  const scopes = ""
  const state = window.crypto.randomUUID();

  const link = new URL("https://id.twitch.tv/oauth2/authorize")
  link.searchParams.append("response_type", "token")
  link.searchParams.append("client_id", clientId)
  link.searchParams.append("redirect_uri", redirectUri)
  link.searchParams.append("scope", scopes)
  link.searchParams.append("state", state)

  window.location.href = link.href;

  return <div className="login">
    <Link to={link.href}>Login with Twitch</Link>
  </div>
}