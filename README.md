# Heroes and Monsters
A RPG, turn based battle game that has heroes that explore a grid map and interact with markets to buy new tools to prepare them to fight monsters.

## Design
See the design files in `/uml` that illustrates how the classes are related

I emphasized extendability by
- not making assumptions about behaviors of heroes that monsters didn't have
- abstracting away superclasses so that most extensions of them could be added with ease
- having methods take the superclass type instead of specific types
- favoring composition over inheritance when possible with interfaces

I emphasized scalability by
- building methods to be efficient and reducing redundant expensive operations
- using docstrings to describe the purpose of less obvious helper functions
- making everything a parameter that can be changed and using class constants instead of magic variables