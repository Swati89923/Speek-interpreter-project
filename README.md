# Speek Interpreter

A **Java-based interpreter for a simple natural language–style programming language called Speek**.  
Speek allows users to write programs using **readable English-like statements** instead of traditional programming syntax.

This project demonstrates the **design and implementation of a programming language interpreter**, including tokenization, parsing, abstract syntax trees (AST), and execution.

---

## Overview

The **Speek Interpreter** converts human-readable instructions into executable actions through the following stages:

1. **Tokenization (Lexical Analysis)** – Converts source code into tokens.
2. **Parsing** – Builds a syntax structure from tokens.
3. **Abstract Syntax Tree (AST)** – Represents the program structure.
4. **Interpretation** – Executes the instructions.

The language supports:

- Variable declarations
- Arithmetic expressions
- Conditional statements
- Loops
- Output statements

The syntax is designed to be **simple and intuitive**, making it suitable for learning how interpreters work.

---

## Example Speek Program

```
let x be 5
let y be 10

if x is less than y then
say "x smaller"
end
```

### Output

```
x smaller
```

---

## Project Structure

```
Speek-interpreter-project
│
├── src/
│   └── speek/
│       ├── AssignInstruction.java
│       ├── BinaryOpNode.java
│       ├── Environment.java
│       ├── Expression.java
│       ├── IfInstruction.java
│       ├── Instruction.java
│       ├── Interpreter.java
│       ├── NumberNode.java
│       ├── Parser.java
│       ├── PrintInstruction.java
│       ├── RepeatInstruction.java
│       ├── StringNode.java
│       ├── Test.java
│       ├── Token.java
│       ├── TokenType.java
│       ├── Tokenizer.java
│       └── VariableNode.java
```

---

## Key Components

### Tokenizer

Responsible for **breaking the source code into tokens**, such as:

- keywords  
- identifiers  
- numbers  
- strings  
- operators  

File: `Tokenizer.java`

---

### Parser

The parser converts tokens into **instructions and expressions**, building an internal representation of the program.

File: `Parser.java`

---

### AST Nodes

The interpreter uses **Abstract Syntax Tree (AST) nodes** to represent expressions.

Examples:

- `NumberNode` – numeric values  
- `StringNode` – string values  
- `VariableNode` – variables  
- `BinaryOpNode` – arithmetic or logical operations  

---

### Instructions

Each language command is represented as an instruction class.

| Instruction | Description |
|-------------|-------------|
| AssignInstruction | Variable assignment |
| PrintInstruction | Output text |
| IfInstruction | Conditional execution |
| RepeatInstruction | Loop execution |

---

### Interpreter

The **Interpreter** executes the parsed instructions sequentially using an **environment** that stores variable values.

File: `Interpreter.java`

---

## Supported Language Features

### Variables

```
let x be 10
```

### Printing Output

```
say "Hello World"
```

### Conditional Statements

```
if x is less than y then
say "x is smaller"
end
```

### Loops

```
repeat 5 times
say "Hello"
end
```

### Arithmetic Expressions

```
let z be x + y
```

---

## How to Run

### 1. Clone the repository

```
git clone https://github.com/your-username/speek-interpreter-project.git
```

### 2. Compile the project

```
javac src/speek/*.java
```

### 3. Run the interpreter

```
java speek.Test
```

---

## Learning Objectives

This project demonstrates key **interpreter and language design concepts**:

- Lexical Analysis
- Parsing
- Abstract Syntax Trees
- Execution Environment
- Programming Language Design

It is useful for understanding concepts taught in:

- Compiler Design
- Programming Languages
- Software Engineering

---

## Future Improvements

Possible enhancements include:

- Adding functions
- Supporting more operators
- Better error handling
- Interactive REPL support
- File-based program execution
- Extended grammar rules

---
