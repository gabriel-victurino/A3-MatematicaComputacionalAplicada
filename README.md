# A3-MatematicaComputacionalAplicada
# 🧾 Calculadora de Imposto de Renda — Brasil 2025

Aplicação desktop em Java com interface gráfica (Swing) que calcula o **Imposto de Renda Retido na Fonte (IRRF)** mensal com base na tabela progressiva de 2025, incluindo a nova regra de isenção para salários brutos até R$ 5.000,00 e suporte a múltiplos vínculos empregatícios.

---

## ✨ Funcionalidades

- ✅ Cálculo do IRRF com a **tabela progressiva de 2025**
- ✅ Aplicação da **nova lei de isenção** para salários brutos até R$ 5.000,00
- ✅ Redutor progressivo para salários entre R$ 5.001,00 e R$ 7.000,00
- ✅ **Detalhamento completo** do cálculo: base, faixa, alíquota, parcelas e redutor
- ✅ Suporte a **múltiplos empregos** com consolidação do imposto real
- ✅ Estimativa de **diferença a pagar ou restituição** na declaração anual

---

## 📐 Tabela Progressiva Mensal (2025)

| Salário bruto | Base de cálculo* | Alíquota | Parcela a deduzir |
|---|---|---|---|
| Até R$ 2.787,20 | Até R$ 2.259,20 | Isento | — |
| R$ 2.787,21 – R$ 3.354,65 | R$ 2.259,21 – R$ 2.826,65 | 7,5% | R$ 169,44 |
| R$ 3.354,66 – R$ 4.279,05 | R$ 2.826,66 – R$ 3.751,05 | 15% | R$ 381,44 |
| R$ 4.279,06 – R$ 5.192,68 | R$ 3.751,06 – R$ 4.664,68 | 22,5% | R$ 662,77 |
| Acima de R$ 5.192,68 | Acima de R$ 4.664,68 | 27,5% | R$ 896,00 |

> *Base de cálculo = Salário bruto − desconto simplificado de R$ 528,00

### Nova Lei de Isenção

| Salário bruto | Efeito |
|---|---|
| Até R$ 5.000,00 | Imposto zerado via redutor integral |
| R$ 5.001,00 – R$ 7.000,00 | Redutor progressivo (isenção parcial decrescente) |
| Acima de R$ 7.000,00 | Tabela progressiva plena, sem redutor |

---

## 🖥️ Como usar

### Pré-requisitos

- Java JDK 8 ou superior
- Nenhuma dependência externa

### Compilação

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/calculadora-ir-brasil.git
cd calculadora-ir-brasil

# Compile todos os arquivos
javac -d out src/modelo/*.java src/principal/*.java
```

### Execução

```bash
java -cp out principal.Principal
```

---

## 🔄 Fluxo da aplicação

```
┌─────────────────────────────────────────┐
│  Quantos vínculos empregatícios?        │
│  [ 1 emprego ]  [ Mais de 1 emprego ]   │
└──────────────┬──────────────────────────┘
               │
       ┌───────┴────────┐
       ▼                ▼
  1 emprego      Múltiplos empregos
       │                │
       │         Nome + salário de cada
       │         emprego (loop)
       │                │
       ▼                ▼
  Detalhamento    Detalhamento individual
  do cálculo      de cada emprego
                        │
                        ▼
                  Resumo consolidado:
                  IR retido × IR real
                  → diferença ou restituição
```

---

## 📂 Estrutura do projeto

```
calculadora-ir-brasil/
├── src/
│   ├── modelo/
│   │   ├── Calculo.java              # Regras de cálculo do IRRF
│   │   ├── ResultadoCalculo.java     # DTO com detalhes de um emprego
│   │   └── ResultadoConsolidado.java # Consolidação de múltiplos empregos
│   └── principal/
│       └── Principal.java            # Interface gráfica (Swing) e fluxo principal
└── README.md
```

### Responsabilidades das classes

**`Calculo.java`**
Centraliza toda a lógica tributária: aplica a tabela progressiva, calcula o redutor da nova lei de isenção e consolida múltiplos vínculos. Método principal: `calcularDetalhado(double salarioBruto)`.

**`ResultadoCalculo.java`**
Objeto de transferência de dados (DTO) que carrega cada etapa do cálculo: salário bruto, desconto simplificado, base de cálculo, faixa, alíquota, imposto antes e depois do redutor, e imposto final.

**`ResultadoConsolidado.java`**
Agrega os resultados de cada empregador e compara com o imposto real calculado sobre a soma de todos os salários, apontando eventual diferença a pagar ou restituição na declaração anual.

**`Principal.java`**
Interface gráfica com `JOptionPane`. Gerencia o fluxo de entrada do usuário, monta as mensagens de detalhamento e exibe o resumo consolidado.

---

## 💡 Exemplo de saída — 1 emprego (R$ 6.500,00)

```
       CÁLCULO DO IMPOSTO DE RENDA
          Nova Tabela 2025
─────────────────────────────────────
📋  BASE DE CÁLCULO
  Salário bruto mensal:       R$ 6.500,00
  (-) Desconto simplificado:  R$   528,00
  (=) Valor base de cálculo:  R$ 5.972,00

📊  FAIXA APLICADA
  27,5% — base acima de R$ 4.664,68

🧮  CÁLCULO DO IMPOSTO
  Imposto bruto (base × 27,5%):  R$ 1.642,30
  (-) Parcela a deduzir tabela:  R$   896,00
  (=) Imposto após tabela:       R$   746,30
  (-) Redutor nova lei (2025):   R$   186,57

─────────────────────────────────────
💰  IMPOSTO A RECOLHER:      R$ 559,73
  Alíquota efetiva:           8,61%
  Salário líquido estimado:   R$ 5.940,27
```

## 💡 Exemplo de saída — 2 empregos

```
  RESUMO CONSOLIDADO — MÚLTIPLOS EMPREGOS
═════════════════════════════════════════
📌  RETENÇÃO POR EMPREGADOR (mensal)
─────────────────────────────────────
  Empresa A
    Salário bruto:  R$ 3.000,00
    IR retido:      R$     0,00        ← isento isoladamente

  Empresa B
    Salário bruto:  R$ 4.000,00
    IR retido:      R$     0,00        ← isento isoladamente

─────────────────────────────────────
  Total bruto (soma):          R$ 7.000,00
  Total IR retido:             R$     0,00

⚠️  IMPOSTO REAL SOBRE RENDA TOTAL
─────────────────────────────────────
  IR devido sobre total:       R$   143,75

═════════════════════════════════════════
  ⚠️  IMPOSTO EXTRA A PAGAR na declaração:
       R$ 143,75
```

> **Por quê isso acontece?** Cada empregador calcula o IR sem saber do outro vínculo. Na declaração anual a Receita Federal soma todos os rendimentos, o que pode elevar a faixa tributável e gerar imposto a pagar.

---

## ⚠️ Aviso legal

Este projeto é um exercício acadêmico. Os valores e alíquotas implementados são baseados na legislação vigente em 2025, mas **não substituem orientação de um contador ou da Receita Federal**. Consulte sempre um profissional para situações reais.

---

## 📜 Licença

Distribuído sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.
