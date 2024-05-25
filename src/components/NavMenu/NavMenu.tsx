import "./NavMenu.css"
import NavLink from "./NavLink.tsx";

export default function NavMenu() {
  return <div className="navMenu">
    NAVBARRRRR
    <NavLink name="Dashboard" target="/"/>
    <NavLink name="Templates" target="/templates"/>
    <NavLink name="Goal" target="/goal"/>
  </div>
}