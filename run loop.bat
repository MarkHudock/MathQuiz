@echo off
title MathQuiz
:loop
	java -cp bin quiz.MathQuiz
	pause
	cls
	goto loop