import {useEffect, useRef, useState} from "react";
import {Message} from "./Message.ts";
import {loadMore} from "./getMessages.ts";
import Loader from "../../LoadingSpinner/Loader.tsx";
import ChatLine from "../ChatLine/ChatLine.tsx";
import ChatHistoryQuery from "../Query/ChatHistoryQuery.tsx";
import "./MessagePane.css"
import VisibilityCallback from "../../VisibilityCallback/VisibilityCallback.tsx";


export default function MessagePane() {
  const [messages, setMessages] = useState<Message[]>([])
  const [isLoading, setLoading] = useState(true);

  const [earliestTimeStamp, setEarliestTimeStamp] = useState(0)
  const containerRef = useRef(null);
  const previousScrollPosition = useRef(0);
  const [query, setQuery] = useState("");

  // useEffect(() => {
  //   getEarlier(6406316591700);
  // }, [])
  //
  // function getEarlier(timeStamp: number) {
  //   console.log("loaded!")
  //   const promise = loadMore(timeStamp);
  //   promise.then(value => {
  //     setLoading(false)
  //     // messages.push(...value);
  //     setMessages(value)
  //     setEarliestTimeStamp(value[value.length - 1].timeStamp)
  //     containerRef.current.scrollTop = previousScrollPosition.current
  //   })
  //   promise.catch(reason => console.log(reason))
  // }
  //
  //
  // function handleLoadMore() {
  //   getEarlier(earliestTimeStamp)
  // }

  function handleQuery(s: string) {

  }


  useEffect(() => {
    function handleScroll(event: any)  {
      console.log("event")
      console.log(event)
      const scrollTop = event.currentTarget.scrollTop
      const scrollHeight = event.currentTarget.scrollHeight
      const clientHeight = event.currentTarget.clientHeight
      // Save the current scroll position
      previousScrollPosition.current = scrollTop;

      // Check if user has scrolled to the bottom
      if (
        scrollTop + clientHeight >=
        scrollHeight
      ) {
        // handleLoadMore();
      }
    }

    containerRef.current.addEventListener('scroll', handleScroll);
    return () => {
      containerRef.current.removeEventListener('scroll', handleScroll);
    };
  }, [messages]); // Re-run effect when items change


// Render
  if (isLoading) {
    return <Loader/>
  }

  return (
    <div className="message-pane">
      <div className="chat_history_header std-font">
        <ChatHistoryQuery value={query} onChange={handleQuery}/>
        <span>CHAT-HISTORY</span>
      </div>
      {/*onScroll={event => handleScroll(event)}*/}
      <div ref={containerRef} className="message-scroll-container">
        {/*<div className="message-list">*/}
          {messages.map((message , index)=> <ChatLine key={index} message={message}/>)}
          {/*{(query.length == 0) ?*/}
          {/*  <VisibilityCallback onInView={() => {*/}
          {/*    console.log("change!!!!!");*/}
          {/*    handleLoadMore();*/}
          {/*  }}/>*/}
          {/*  : <></>*/}
          {/*}*/}
        {/*</div>*/}
      </div>
    </div>
  )
}