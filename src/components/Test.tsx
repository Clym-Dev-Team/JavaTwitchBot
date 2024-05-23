import {useState, useRef, useEffect} from 'react';
import {Message} from "./ChatHistoryPane/Message.ts";
import {loadMore} from "./ChatHistoryPane/getMessages.ts";
import "./ChatHistoryPane/MessagePane.css"
import ChatHistoryQuery from "./ChatHistoryQuery/ChatHistoryQuery.tsx";
import ChatLine from "./ChatLine/ChatLine.tsx";


function InfiniteScroll() {
  const [items, setItems] = useState<Message[]>([]);
  const [isLoading, setLoading] = useState(true);
  const [earliestTimeStamp, setEarliestTimeStamp] = useState(6406316591700)
  const [query, setQuery] = useState("");
  const containerRef = useRef(null);
  const previousScrollPosition = useRef(0);

  function getEarlier(timeStamp: number) {
    console.log("loaded!")
    const promise = loadMore(timeStamp);
    promise.then(value => {
      setLoading(false)
      items.push(...value);
      // setItems(value)
      // setEarliestTimeStamp(value[value.length - 1].timeStamp)
      containerRef.current.scrollTop = previousScrollPosition.current
    })
    promise.catch(reason => console.log(reason))
  }

  function handleQuery(s: string) {

  }

  useEffect(() => {
    console.log('Container Ref:', containerRef.current);

    const handleScroll = () => {
      // Save the current scroll position
      previousScrollPosition.current = containerRef.current.scrollTop;

      // Check if user has scrolled to the bottom
      if (
        containerRef.current.scrollTop + containerRef.current.clientHeight >= containerRef.current.scrollHeight
      ) {
        getEarlier(earliestTimeStamp);
      }
    };

    containerRef.current.addEventListener('scroll', handleScroll);
    return () => {
      containerRef.current.removeEventListener('scroll', handleScroll);
    };
  }, [items]); // Re-run effect when items change

  useEffect(() => {
    if (items.length == 0) {
      getEarlier(earliestTimeStamp);
    }
  }, []);

  return (
    <div className="message-pane">
      <div className="chat_history_header std-font">
        <ChatHistoryQuery value={query} onChange={handleQuery}/>
        <span>CHAT-HISTORY</span>
      </div>
      <div ref={containerRef} style={{height: '400px', overflowY: 'scroll'}}>
        {/*{items.map((item, index) => (*/}
        {/*  <div key={index}>/!* Render item content here *!/</div>*/}
        {/*))}*/}
        {items.map((message , index)=> <ChatLine key={index} message={message}/>)}
      </div>
    </div>
  );
}

export default InfiniteScroll;
