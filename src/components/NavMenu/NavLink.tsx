import {useLocation} from "react-router-dom";
import React, {useEffect} from "react";
import "./NavLink.css"

export interface NavLinkProps {
  target: string,
  name: string
}

export default function NavLink({target, name}: NavLinkProps) {
  const {pathname} = useLocation();
  const [highlight, setHighlight] = React.useState(false);
  console.log(`path: ${pathname}`);

  useEffect(() => {
    setHighlight(pathname == target);
  }, [pathname, target]);

  return <a href={target} className={"navLink " + (highlight ? "navLinkHighlight" : "")}>{name}</a>
}