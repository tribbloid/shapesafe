## Shapes

### Acronyms

- Static -> S
- Unchecked -> Uc
- Dynamic -> D
- Hybrid -> H

### Proposal 1

- LeafShape
    - Static
        - Eye
        - `><` (cons)
    - Unchecked
    - Var
- Conjecture1
    - Static -> Static
    - Unchecked -> Unchecked
- Conjecture2
  
    | \    | S    | Uc   |
    | ---- | ---- | ---- |
    | S    | S    | Uc   |
    | Uc   | Uc   | Uc   |

Only Unchecked participates in reduction.

Var will be kept as expressions, most notably OuterProduct.

(This however, may mark some ill-defined Shape conjecture as valid) TODO: need more example

### Proposal 2 (abandoned, too complex)

- LeafShape
    - Empty
    - Hybrid
    - Dynamic
    - `><`
    
- Conjecture1

    - AppendNewName
      - S -> S >< Arity
      - H -> H >< UcArity
      - D -> D >< UcArity
      - 
      
    - SquashOldName

      - S -> S ~ Arity
      - H -> H ~ UcArity
      - D -> D ~ UcArity
      
    - Select1

      - S -> Empty >< Arity
      - H -> Dynamic >< Arity
      - D -> Dynamic >< Arity

- Conjecture2

    - OuterProduct
      
        | \    | S    | H    | D    |
        | ---- | ---- | ---- | ---- |
        | S    | S    | H    |      |
        | H    | H    |      |      |
        | D    |      |      |      |
        
    - DimensionWise (we may need to redefine this operator)
    
        | \    | S    | H    | D    |
        | ---- | ---- | ---- | ---- |
        | S    | S    | H    |      |
        | H    | H    |      |      |
        | D    |      |      |      |
    
        