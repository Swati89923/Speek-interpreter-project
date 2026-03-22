# Speek-interpreter-project
Advanced OOP Mini Interpreter
# 🎯 SPEEK Interpreter — Advanced OOP Group Project  

This project is part of the **Advanced Object Oriented Programming (Java)** coursework.  
Our team is building a **Mini Interpreter** for the toy scripting language **SPEEK (Simple Plain English Execution Kit)**.

The interpreter reads source code written in SPEEK language, parses it, and executes the instructions to produce output.

---

## 👩‍💻 Team Members  

- Swati Kumari (Parser Module)
- Vinay Kumar (Tokenizer Module)
- Ajay Jatav (Evaluator / Instruction Module)

---

## 🚀 Project Objective  

The main goal of this project is to understand how programming languages work internally by implementing:

- Tokenization (Lexical Analysis)
- Parsing (Syntax Tree Construction)
- Evaluation / Execution (Runtime behaviour)

This helps in developing strong **Object Oriented Design skills**,  
such as class hierarchy design, abstraction, encapsulation, and responsibility separation.

---

## 🧠 How Our Interpreter Works  

The interpreter follows a **3-step pipeline architecture**:

1️⃣ **Tokenizer**  
Reads the raw source code character-by-character and converts it into a list of tokens.

2️⃣ **Parser**  
Reads the token list and builds an **Expression Tree / Instruction Structure** that represents program logic.

3️⃣ **Evaluator (Interpreter Runtime)**  
Traverses the tree and executes instructions using a shared **Environment (variable store)**.

---

## 🗂️ Project Structure  

```speek-interpreter
│
├── tokenizer → Tokenizer & Token classes
├── parser → Parser & Expression Node classes
├── instruction → Assign / Print / If / Repeat instructions
├── environment → Variable storage logic
├── interpreter → Pipeline controller
└── main → CLI entry point 


 ```

## ▶️ Sample SPEEK Program  

 ``` let x be 10
let y be 3
let result be x + y * 2
say result  
 ```

✅ Expected Output  
16


---

## ⚙️ How to Run the Project  

1. Clone the repository  


2. Open project in any Java IDE (VS Code / IntelliJ / Eclipse)

3. Compile and run the `Main` class  

4. Provide `.speek` source file path as command line argument  

---

##  Contribution Guidelines  

- Always **pull latest changes** before starting work  
- Create **small meaningful commits**  
- Use clear commit messages  
- Do not push broken code to main branch  

Example:
git pull
git add .
git commit -m "Parser expression handling added"
git push


---

## ⭐ Learning Outcomes  

Through this project we aim to learn:

- Real-world OOP design  
- Interpreter architecture basics  
- Expression tree & recursion concepts  
- Clean modular code collaboration using GitHub  

---

✨ *This project is developed for academic learning and conceptual understanding of how programming languages execute internally.*
