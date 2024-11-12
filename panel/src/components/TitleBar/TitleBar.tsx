import {ReactNode} from "react";
import "./TitleBar.css"

export interface TitleBarProps {
  returnBtn?: ReactNode;
  title: ReactNode;
}

export default function TitleBar({title, returnBtn}: TitleBarProps) {
  return <div className="titleBar">
    { returnBtn && <div className="titleBar-returnBtn">{returnBtn}</div>}
    <div className="titleBar-title">{title}</div>
  </div>
}