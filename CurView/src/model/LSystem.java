package model;

import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;

public abstract class LSystem<T>
{
	public abstract Collection<T> build(T origin, int iterations);
	protected abstract T doStackPush(T obj, Deque<T> stack);
	protected abstract T doStackPop(Deque<T> stack);
	
	protected enum operation
	{
		TURN_LEFT,
		TURN_RIGHT,
		MOVE_FWD_DRAW,
		MOVE_FWD_NODRAW,
		PUSH_STATE,
		POP_STATE
	};

	/**
	 * set of symbols that cannot be replaced
	 */
	protected HashMap<String, operation> constants;
	/**
	 * set of symbols that can be replaced
	 */
	protected HashMap<String, String> variables;
	protected String axiom;
	protected float turnAngleDegrees;
	
	public LSystem()
	{
		constants = new HashMap<>();
		variables = new HashMap<>();
	}

	protected String buildStringSequence(int iterations) throws OutOfMemoryError
	{
		StringBuilder output = new StringBuilder(axiom);
		for(int i = 0; i < iterations; ++i)
		{
			StringBuilder tmp = new StringBuilder();
			
			for(int j = 0; j < output.length(); ++j)
			{
				String cstr = String.valueOf(output.charAt(j));
				String successor = variables.get(cstr);
				if(successor != null)
				{
					tmp.append( successor );
				}
				else
				{
					tmp.append(cstr);
				}
			}
			
			output = new StringBuilder(tmp);
		}
		return output.toString();
	}
}