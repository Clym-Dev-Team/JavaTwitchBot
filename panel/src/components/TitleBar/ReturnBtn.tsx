import "./ReturnBtn.css"
import {Link} from "react-router-dom";

export interface ReturnBtnProps {
  link: string,
  iconLink?: string,
  text: string,
}

export default function ReturnBtn({link, iconLink, text}: ReturnBtnProps) {
  return <Link className="returnBtn" to={link}>
    {iconLink && <img className="returnBtn-icon" src={iconLink} alt="return Icon" />}
    <span className="returnBtn-text">{text}</span>
  </Link>
}