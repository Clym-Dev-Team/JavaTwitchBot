import {ReactNode} from "react";

export interface ReturnBtnProps {
  link: string,
  icon?: ReactNode,
  text: string,
}

export default function ReturnBtn({link, icon, text}: ReturnBtnProps) {
  return <a className="returnBtn" href={link}>
    {icon && icon}
    {text}
  </a>
}