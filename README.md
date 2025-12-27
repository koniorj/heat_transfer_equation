# 1D Heat Transport Solver (FEM)

This project implements a Finite Element Method (FEM) solver for a one-dimensional steady-state heat transport problem.

## Problem Overview
The objective is to find the temperature distribution $u(x)$ along a bar of length $L=3$.

### Governing Equation
The steady-state heat conduction is described by the following differential equation:
$$-k(x) \frac{d^2u}{dx^2} = 0, \quad x \in (0, 3)$$

### Thermal Conductivity $k(x)$
The conductivity coefficient is piecewise constant across the domain:
* $k(x) = 0.5$ for $x \in [0, 1]$
* $k(x) = 1.0$ for $x \in (1, 3]$

### Boundary Conditions
1.  **Left boundary ($x=0$):** Robin boundary condition
    $$\frac{du(0)}{dx} - u(0) = 1$$
2.  **Right boundary ($x=3$):** Dirichlet boundary condition
    $$u(3) = 3$$

## Mathematical Model (Weak Form)
The problem is solved using the variational (weak) formulation derived as:
$$\int_0^3 k(x)u'(x)v'(x) \, dx + 0.5u(0)v(0) = -0.5v(0)$$

## Numerical Implementation
1.  **Discretization:** The domain $[0, 3]$ is divided into $n$ finite elements.
2.  **Integration:** Numerical integration is performed using 2-point Gauss-Legendre quadrature.
3.  **Solver:** A linear system of equations $\mathbf{K}\mathbf{u} = \mathbf{F}$ is assembled and solved in Kotlin.

## Technologies
* **Language:** Kotlin (JDK 21)
* **Build Tool:** Gradle
* **IDE:** IntelliJ IDEA
