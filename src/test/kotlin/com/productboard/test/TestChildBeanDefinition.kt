package com.productboard.test

import com.productboard.modulith.PublicModuleInterface
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

interface X : PublicModuleInterface

@Primary @Component class X1 : X

@Component class X2 : X
