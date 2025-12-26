# Finite Element Method (FEM) Solver in Kotlin

## Problem Description: 1D Heat Transport
This project implements a Finite Element Method (FEM) solver for a 1D heat conduction problem. The goal is to find the temperature distribution $u(x)$ along a bar of length $L=3$.

### Differential Equation
The steady-state heat equation is given by:
$$-\frac{d}{dx} \left( k(x) \frac{du}{dx} \right) = 0$$

where $k(x)$ is the thermal conductivity coefficient:
- $k(x) = 0.5$ for $x \in [0, 1]$
- $k(x) = 1.0$ for $x \in (1, 3]$

### Boundary Conditions
The system is subject to the following conditions:
1. **Robin Boundary Condition** at $x=0$:
   $$\frac{du(0)}{dx} - u(0) = 1$$
2. **Dirichlet Boundary Condition** at $x=3$:
   $$u(3) = 3$$

## Mathematical Formulation (Variational Form)
To solve this using FEM, we derive the weak form:
$$\int_{0}^{3} k(x) u'(x) v'(x) dx + k(0)u(0)v(0) = -k(0)v(0)$$
*(Note: Full derivation steps will be added to the project documentation.)*

## Implementation Goals
- [ ] Derive the variational formulation.
- [ ] Implement a mesh generator with $n$ elements.
- [ ] Use **Gauss-Legendre Quadrature** for numerical integration.
- [ ] Solve the resulting linear system $\mathbf{K}\mathbf{u} = \mathbf{F}$.
- [ ] Visualize the results using external tools.
