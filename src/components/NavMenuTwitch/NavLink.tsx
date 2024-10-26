import {Link, useLocation} from "react-router-dom";
import React, {useEffect} from "react";
import "./NavLink.css"

export interface NavLinkProps {
  target: string,
  allowChildren?: boolean,
  name: string
}

export default function NavLink({target, allowChildren, name}: NavLinkProps) {
  const {pathname} = useLocation();
  const [highlight, setHighlight] = React.useState(false);

  useEffect(() => {
    if (allowChildren == undefined || allowChildren) {
      setHighlight(pathname.startsWith(target));
    } else {
      setHighlight(pathname == target);
    }
  }, [pathname, target]);

  return <Link to={target} className={"navLink " + (highlight ? "navLinkHighlight" : "")}>{name}</Link>
}