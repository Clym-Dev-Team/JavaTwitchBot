import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import './main.css'
import {GoalEditorPane} from "./components/Goals/GoalEditor.tsx";
import Test from "./components/Test.tsx";
import {TemplatePane} from "./components/Templates/TemplatePane/TemplatePane.tsx";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import MessagePane from "./components/ChatHistory/Pane/MessagePane.tsx";

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/goal" element={<GoalEditorPane/>}/>
        <Route path="/template" element={<TemplatePane/>}/>
        <Route path="/test" element={<Test/>}/>
        <Route path="/history" element={<MessagePane/>}/>
      </Routes>
    </BrowserRouter>
  </React.StrictMode>,
)
