import TwitchMenuTile from "./TwitchMenuTile.tsx";
import "./TwitchNavMenu.css"
import IconTimer from "../../assets/IconTimer.tsx";
import IconMegaphone from "../../assets/IconMegaphone.tsx";
import IconTextBox from "../../assets/IconTextBod.tsx";
import IconGift from "../../assets/IconGift.tsx";
import NavLink from "../NavMenu/NavLink.tsx";

export default function TwitchNavMenu() {
  return <div className="navMenu">
    <div className="twitchTiles">
      <TwitchMenuTile icon={<IconTextBox/>} label="Commands" target="/"/>
      <TwitchMenuTile icon={<IconGift/>} label="Giveaways" target="/"/>
      <TwitchMenuTile icon={<IconTimer/>} label="Timer" target="/"/>
      <TwitchMenuTile icon={<IconMegaphone/>} label="Alerts" target="/"/>
    </div>
    <div className="otherOptions">
      <NavLink target="/" name="WebConsole"/>
      <NavLink target="/goal" name="Goals"/>
      <NavLink target="/templates" name="Templates"/>
      <NavLink target="/test" name="Message History Test"/>
      <NavLink target="/commands" name="Command Popup"/>
    </div>
    <div className="account">
      {/*
      - username
      - your twitch profile picture
      (not sure if we will sink the panel accs to twitch acc's, but we wouldn't need that for this. We could also just have a twitch name field in the account creation panel, and the get the picture for that account
      */}
    </div>
  </div>
}