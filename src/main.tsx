import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import './main.css'
import {GoalEditorPane} from "./components/Goals/GoalEditor.tsx";
import Test from "./components/Test.tsx";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import MessagePane from "./components/ChatHistory/Pane/MessagePane.tsx";
import TemplateEditorPane from "./components/Templates/TemplateEditor/TemplateEditorPane.tsx";
import TemplateListPane from "./components/Templates/TemplateList/TemplateListPane.tsx";

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/goal" element={<GoalEditorPane/>}/>
        <Route path="/templates" element={<TemplateListPane/>}/>
        <Route path="/templates/edit" element={<TemplateEditorPane/>}/>
        <Route path="/test" element={<Test/>}/>
        <Route path="/history" element={<MessagePane/>}/>
      </Routes>
    </BrowserRouter>
  </React.StrictMode>,
)
