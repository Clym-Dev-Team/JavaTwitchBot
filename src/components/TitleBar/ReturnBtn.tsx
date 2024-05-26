import "./ReturnBtn.css"

export interface ReturnBtnProps {
  link: string,
  iconLink?: string,
  text: string,
}

export default function ReturnBtn({link, iconLink, text}: ReturnBtnProps) {
  return <a className="returnBtn" href={link}>
    {iconLink && <img className="returnBtn-icon" src={iconLink} alt="return Icon" />}
    <span className="returnBtn-text">{text}</span>
  </a>
}