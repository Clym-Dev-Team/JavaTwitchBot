import "./TwitchMenuTile.css"
import React, {useEffect} from "react";
import {Tooltip, TooltipContent, TooltipTrigger} from "@shadcn/components/ui/tooltip.tsx";
import {useLocation} from "react-router-dom";

export interface TwitchMenuTileProps {
  icon: React.ReactNode
  label: string;
  target: string
}

export default function TwitchMenuTile({icon, target, label}: TwitchMenuTileProps) {
  const {pathname} = useLocation();
  const [highlight, setHighlight] = React.useState(false);

  useEffect(() => {
    setHighlight(pathname == target);
  }, [pathname, target]);

  return <Tooltip>
    <TooltipContent>
      <span className="tooltipContent">{label}</span>
    </TooltipContent>
    <TooltipTrigger>
      <a href={target} className={"twitchMenuTile" + (highlight ? "highlight" : "")}>
        {icon}
      </a>
    </TooltipTrigger>
  </Tooltip>
}