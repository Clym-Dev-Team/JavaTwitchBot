import useOnScreen from "./useOnScreen.ts";
import {useEffect, useRef, useState} from "react";
import Loader from "../LoadingSpinner/Loader.tsx";

interface props {
  onInView: () => void;
}

export default function VisibilityCallback(props: props) {
  const elementRef = useRef<HTMLDivElement>(null);
  const [onScreenBevor, setonScreenBevor] = useState<boolean>()
  const isOnScreen = useOnScreen(elementRef);

  useEffect(() => {
    if (!onScreenBevor && isOnScreen) {
      props.onInView();
    }
    setonScreenBevor(isOnScreen)
  }, [isOnScreen]);

  return (<div id="visibilityCallback" ref={elementRef}><Loader/></div>)
}